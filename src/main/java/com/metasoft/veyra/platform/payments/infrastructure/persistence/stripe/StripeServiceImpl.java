package com.metasoft.veyra.platform.payments.infrastructure.persistence.stripe;

import com.metasoft.veyra.platform.payments.domain.model.valueobjects.PlanType;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.SubscriptionPeriod;
import com.metasoft.veyra.platform.payments.infrastructure.persistence.stripe.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StripeServiceImpl implements StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    private final Map<String, String> priceIds = new HashMap<>();
    private final Map<String, String> productIds = new HashMap<>();

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
        log.info("Initializing Stripe with API key");
        initializePrices();
    }

    private void initializePrices() {
        try {
            loadOrCreateProduct("Family Plan", PlanType.FAMILY);
            loadOrCreateProduct("Nursing Home Plan", PlanType.NURSING_HOME);
            log.info("Stripe prices initialized successfully");
        } catch (StripeException e) {
            log.error("Error initializing Stripe prices: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize Stripe prices", e);
        }
    }

    private void loadOrCreateProduct(String name, PlanType planType) throws StripeException {
        log.info("Loading or creating product and prices for: {}", name);

        // Buscar productos existentes
        ProductSearchParams searchParams = ProductSearchParams.builder()
                .setQuery("name:'" + name + "' AND active:'true'")
                .setLimit(1L)
                .build();

        ProductSearchResult searchResult = Product.search(searchParams);
        Product product;

        if (!searchResult.getData().isEmpty()) {
            product = searchResult.getData().get(0);
            productIds.put(planType.name(), product.getId());
            log.info("Found existing product: {} with ID: {}", name, product.getId());

            // Cargar precios existentes
            loadExistingPrices(product.getId(), planType, name);
        } else {
            // Crear nuevo producto
            product = createNewProduct(name, planType);
            log.info("Created new product: {} with ID: {}", name, product.getId());
        }
    }

    private Product createNewProduct(String name, PlanType planType) throws StripeException {
        ProductCreateParams productParams = ProductCreateParams.builder()
                .setName(name)
                .setDescription("Subscription plan for " + name)
                .putMetadata("planType", planType.name())
                .build();

        Product product = Product.create(productParams);
        productIds.put(planType.name(), product.getId());

        // Crear precios para el nuevo producto
        createPricesForProduct(product.getId(), planType, name);

        return product;
    }

    private void loadExistingPrices(String productId, PlanType planType, String name) throws StripeException {
        PriceListParams listParams = PriceListParams.builder()
                .setProduct(productId)
                .setActive(true)
                .build();

        PriceCollection prices = Price.list(listParams);

        boolean hasMonthly = false;
        boolean hasAnnual = false;

        for (Price price : prices.getData()) {
            if (price.getRecurring() != null) {
                String interval = price.getRecurring().getInterval();
                if ("month".equals(interval)) {
                    priceIds.put(planType.name() + "_MONTHLY", price.getId());
                    hasMonthly = true;
                    log.info("Loaded existing monthly price: {}", price.getId());
                } else if ("year".equals(interval)) {
                    priceIds.put(planType.name() + "_ANNUALLY", price.getId());
                    hasAnnual = true;
                    log.info("Loaded existing annual price: {}", price.getId());
                }
            }
        }

        // Crear precios faltantes
        if (!hasMonthly) {
            createMonthlyPrice(productId, planType, name);
        }
        if (!hasAnnual) {
            createAnnualPrice(productId, planType, name);
        }
    }

    private void createPricesForProduct(String productId, PlanType planType, String name) throws StripeException {
        createMonthlyPrice(productId, planType, name);
        createAnnualPrice(productId, planType, name);
    }

    private void createMonthlyPrice(String productId, PlanType planType, String name) throws StripeException {
        PriceCreateParams monthlyPriceParams = PriceCreateParams.builder()
                .setProduct(productId)
                .setUnitAmount((long) (planType.getMonthlyPrice() * 100))
                .setCurrency("usd")
                .setRecurring(
                        PriceCreateParams.Recurring.builder()
                                .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
                                .build()
                )
                .setNickname(name + " - Monthly")
                .build();

        Price monthlyPrice = Price.create(monthlyPriceParams);
        priceIds.put(planType.name() + "_MONTHLY", monthlyPrice.getId());
        log.info("Created monthly price for {}: {}", name, monthlyPrice.getId());
    }

    private void createAnnualPrice(String productId, PlanType planType, String name) throws StripeException {
        PriceCreateParams annualPriceParams = PriceCreateParams.builder()
                .setProduct(productId)
                .setUnitAmount((long) (planType.getAnnualPrice() * 100))
                .setCurrency("usd")
                .setRecurring(
                        PriceCreateParams.Recurring.builder()
                                .setInterval(PriceCreateParams.Recurring.Interval.YEAR)
                                .build()
                )
                .setNickname(name + " - Annual")
                .build();

        Price annualPrice = Price.create(annualPriceParams);
        priceIds.put(planType.name() + "_ANNUALLY", annualPrice.getId());
        log.info("Created annual price for {}: {}", name, annualPrice.getId());
    }

    @Override
    public Customer createOrGetCustomer(Long userId, String email) {
        try {
            log.info("Creating or retrieving customer for userId: {}", userId);
            CustomerSearchParams searchParams = CustomerSearchParams.builder()
                    .setQuery("metadata['userId']:'" + userId + "'")
                    .build();
            CustomerSearchResult result = Customer.search(searchParams);

            if (!result.getData().isEmpty()) {
                Customer existingCustomer = result.getData().get(0);
                log.info("Found existing customer: {}", existingCustomer.getId());
                return existingCustomer;
            }

            CustomerCreateParams.Builder paramsBuilder = CustomerCreateParams.builder()
                    .putMetadata("userId", userId.toString());

            if (email != null && !email.isBlank()) {
                paramsBuilder.setEmail(email);
            }

            Customer newCustomer = Customer.create(paramsBuilder.build());
            log.info("Created new customer: {}", newCustomer.getId());
            return newCustomer;

        } catch (StripeException e) {
            log.error("Error creating Stripe customer for userId {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Error creating Stripe customer: " + e.getMessage(), e);
        }
    }

    @Override
    public com.stripe.model.Subscription createSubscription(
            String customerId,
            PlanType planType,
            SubscriptionPeriod period,
            String paymentMethodId) {
        try {
            log.info("Creating subscription for customer: {}, plan: {}, period: {}",
                    customerId, planType, period);

            // Adjuntar método de pago al cliente
            PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
            paymentMethod.attach(PaymentMethodAttachParams.builder()
                    .setCustomer(customerId)
                    .build());
            log.info("Payment method {} attached to customer {}", paymentMethodId, customerId);

            // Establecer como método de pago predeterminado
            Customer customer = Customer.retrieve(customerId);
            customer.update(CustomerUpdateParams.builder()
                    .setInvoiceSettings(
                            CustomerUpdateParams.InvoiceSettings.builder()
                                    .setDefaultPaymentMethod(paymentMethodId)
                                    .build()
                    )
                    .build());

            String priceId = getPriceId(planType, period);
            log.info("Using price ID: {}", priceId);

            SubscriptionCreateParams params = SubscriptionCreateParams.builder()
                    .setCustomer(customerId)
                    .addItem(
                            SubscriptionCreateParams.Item.builder()
                                    .setPrice(priceId)
                                    .build()
                    )
                    .setPaymentBehavior(SubscriptionCreateParams.PaymentBehavior.DEFAULT_INCOMPLETE)
                    .setPaymentSettings(
                            SubscriptionCreateParams.PaymentSettings.builder()
                                    .setPaymentMethodTypes(
                                            List.of(
                                                    SubscriptionCreateParams.PaymentSettings.PaymentMethodType.CARD
                                            )
                                    )
                                    .build()
                    )
                    .addExpand("latest_invoice.payment_intent")
                    .putMetadata("planType", planType.name())
                    .putMetadata("period", period.name())
                    .build();

            com.stripe.model.Subscription subscription = com.stripe.model.Subscription.create(params);
            log.info("Subscription created successfully: {}", subscription.getId());
            return subscription;

        } catch (StripeException e) {
            log.error("Error creating Stripe subscription: {}", e.getMessage(), e);
            throw new RuntimeException("Error creating Stripe subscription: " + e.getMessage(), e);
        }
    }

    @Override
    public com.stripe.model.Subscription updateSubscription(
            String subscriptionId,
            PlanType planType,
            SubscriptionPeriod period) {
        try {
            log.info("Updating subscription: {} to plan: {}, period: {}",
                    subscriptionId, planType, period);

            com.stripe.model.Subscription subscription =
                    com.stripe.model.Subscription.retrieve(subscriptionId);

            String newPriceId = getPriceId(planType, period);
            String currentItemId = subscription.getItems().getData().get(0).getId();

            SubscriptionUpdateParams params = SubscriptionUpdateParams.builder()
                    .addItem(
                            SubscriptionUpdateParams.Item.builder()
                                    .setId(currentItemId)
                                    .setPrice(newPriceId)
                                    .build()
                    )
                    .setProrationBehavior(SubscriptionUpdateParams.ProrationBehavior.CREATE_PRORATIONS)
                    .putMetadata("planType", planType.name())
                    .putMetadata("period", period.name())
                    .build();

            com.stripe.model.Subscription updatedSubscription = subscription.update(params);
            log.info("Subscription updated successfully: {}", subscriptionId);
            return updatedSubscription;

        } catch (StripeException e) {
            log.error("Error updating Stripe subscription {}: {}", subscriptionId, e.getMessage(), e);
            throw new RuntimeException("Error updating Stripe subscription: " + e.getMessage(), e);
        }
    }

    @Override
    public com.stripe.model.Subscription cancelSubscription(String subscriptionId) {
        try {
            log.info("Canceling subscription: {}", subscriptionId);

            com.stripe.model.Subscription subscription =
                    com.stripe.model.Subscription.retrieve(subscriptionId);

            com.stripe.model.Subscription canceledSubscription = subscription.cancel();
            log.info("Subscription canceled successfully: {}", subscriptionId);
            return canceledSubscription;

        } catch (StripeException e) {
            log.error("Error canceling Stripe subscription {}: {}", subscriptionId, e.getMessage(), e);
            throw new RuntimeException("Error canceling Stripe subscription: " + e.getMessage(), e);
        }
    }

    @Override
    public com.stripe.model.Subscription retrieveSubscription(String subscriptionId) {
        try {
            log.info("Retrieving subscription: {}", subscriptionId);
            return com.stripe.model.Subscription.retrieve(subscriptionId);
        } catch (StripeException e) {
            log.error("Error retrieving Stripe subscription {}: {}", subscriptionId, e.getMessage(), e);
            throw new RuntimeException("Error retrieving Stripe subscription: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentIntent createPaymentIntent(Long amountInCents, String currency, String customerId) {
        try {
            log.info("Creating payment intent: amount={}, currency={}, customer={}",
                    amountInCents, currency, customerId);

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency(currency)
                    .setCustomer(customerId)
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .addExpand("latest_charge")
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);
            log.info("Payment intent created: {}", paymentIntent.getId());
            return paymentIntent;

        } catch (StripeException e) {
            log.error("Error creating payment intent: {}", e.getMessage(), e);
            throw new RuntimeException("Error creating payment intent: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentIntent retrievePaymentIntent(String paymentIntentId) {
        try {
            log.info("Retrieving payment intent: {}", paymentIntentId);
            return PaymentIntent.retrieve(paymentIntentId);
        } catch (StripeException e) {
            log.error("Error retrieving payment intent {}: {}", paymentIntentId, e.getMessage(), e);
            throw new RuntimeException("Error retrieving payment intent: " + e.getMessage(), e);
        }
    }

    @Override
    public String getPriceId(PlanType planType, SubscriptionPeriod period) {
        String key = planType.name() + "_" + period.name();
        String priceId = priceIds.get(key);

        if (priceId == null) {
            throw new IllegalArgumentException(
                    "Price not found for plan: " + planType + ", period: " + period
            );
        }

        return priceId;
    }
}
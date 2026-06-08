package com.metasoft.veyra.platform.payments.interfaces.rest;

import com.metasoft.veyra.platform.payments.domain.model.queries.GetPaymentByIdQuery;
import com.metasoft.veyra.platform.payments.domain.services.PaymentQueryService;
import com.metasoft.veyra.platform.payments.interfaces.rest.resources.PaymentResource;
import com.metasoft.veyra.platform.payments.interfaces.rest.transform.PaymentResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST Controller for managing individual payment resources.
 * Handles operations on specific payments by ID.
 */
@RestController
@RequestMapping(value = "/api/v1/payments", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Payments", description = "Endpoints for managing payment resources")
public class PaymentsController {

    private final PaymentQueryService paymentQueryService;

    public PaymentsController(PaymentQueryService paymentQueryService) {
        this.paymentQueryService = paymentQueryService;
    }

    /**
     * GET /api/v1/payments/{paymentId}
     * Retrieves a specific payment by its ID.
     */
    @GetMapping("/{paymentId}")
    @Operation(
            summary = "Get payment by ID",
            description = "Returns detailed information about a specific payment, including status, amount, and receipt URL."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment found"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @Parameter(name = "paymentId", description = "The unique identifier of the payment", required = true)
    public ResponseEntity<PaymentResource> getPaymentById(@PathVariable Long paymentId) {
        var paymentOpt = paymentQueryService.findById(
                new GetPaymentByIdQuery(paymentId)
        );

        if (paymentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var paymentResource = PaymentResourceFromEntityAssembler
                .toResourceFromEntity(paymentOpt.get());

        return ResponseEntity.ok(paymentResource);
    }
}
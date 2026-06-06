
        package com.metasoft.veyra.platform.activities.domain.model.valueobjects;

        import java.time.LocalTime;
        import java.util.Objects;

        public class ActivityPeriod {

            private final LocalTime startTime;
            private final LocalTime endTime;

            public ActivityPeriod(LocalTime startTime, LocalTime endTime) {
                this.startTime = startTime;
                this.endTime = endTime;
            }

            public LocalTime getStartTime() {
                return startTime;
            }

            public LocalTime getEndTime() {
                return endTime;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof ActivityPeriod that)) return false;
                return Objects.equals(startTime, that.startTime) &&
                       Objects.equals(endTime, that.endTime);
            }

            @Override
            public int hashCode() {
                return Objects.hash(startTime, endTime);
            }
        }
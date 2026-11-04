import json
from kafka import KafkaConsumer
from config import KAFKA_BOOTSTRAP_SERVERS, KAFKA_TOPIC, KAFKA_GROUP_ID
from model import calculate_performance
from database import get_current_metrics, upsert_performance


def start_consumer():
    print(f"[KAFKA] Starting consumer on topic: {KAFKA_TOPIC}")

    consumer = KafkaConsumer(
        KAFKA_TOPIC,
        bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
        group_id=KAFKA_GROUP_ID,
        auto_offset_reset='earliest',
        value_deserializer=lambda m: json.loads(m.decode('utf-8'))
    )

    for message in consumer:
        event = message.value
        employee_id = event.get("employeeId")
        event_type = event.get("eventType")
        platform = event.get("platform")

        print(f"[EVENT] Received: {event_type} for employee {employee_id}")

        # Fetch current accumulated metrics
        metrics = get_current_metrics(employee_id)

        # Update metrics based on event type
        if event_type == "COMMIT":
            metrics["commits"] += 1
        elif event_type == "PR_MERGED":
            metrics["pr_merged"] += 1
        elif event_type == "PR_CLOSED_UNMERGED":
            metrics["pr_conflicts"] += 1
        elif event_type == "TEAMS_ACTIVE_CHECK":
            # Simulate a small responsiveness boost
            metrics["teams_score"] = min(metrics["teams_score"] + 0.5, 100)

        # Run through PyTorch model
        perf_score, bonus = calculate_performance(
            metrics["commits"],
            metrics["pr_merged"],
            metrics["pr_conflicts"],
            metrics["teams_score"]
        )

        # Write back to PostgreSQL
        upsert_performance(
            employee_id,
            metrics["commits"],
            metrics["pr_merged"],
            metrics["pr_conflicts"],
            metrics["teams_score"],
            perf_score,
            bonus
        )


if __name__ == "__main__":
    start_consumer()
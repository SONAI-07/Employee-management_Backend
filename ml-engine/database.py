import psycopg2
from config import DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD


def upsert_performance(employee_id, commits, pr_merged, pr_conflicts, teams_score, perf_score, bonus):
    """
    Inserts or updates the performance_metrics table.
    """
    conn = None
    try:
        conn = psycopg2.connect(
            host=DB_HOST,
            port=DB_PORT,
            database=DB_NAME,
            user=DB_USER,
            password=DB_PASSWORD
        )
        cursor = conn.cursor()

        # Check if record exists
        cursor.execute(
            "SELECT id FROM performance_metrics WHERE employee_id = %s",
            (employee_id,)
        )
        existing = cursor.fetchone()

        if existing:
            cursor.execute("""
                UPDATE performance_metrics
                SET total_commits = %s,
                    total_pr_merged = %s,
                    total_pr_conflicts = %s,
                    teams_responsiveness_score = %s,
                    performance_score = %s,
                    bonus_amount = %s,
                    last_updated = NOW()
                WHERE employee_id = %s
            """, (commits, pr_merged, pr_conflicts, teams_score, perf_score, bonus, employee_id))
        else:
            cursor.execute("""
                INSERT INTO performance_metrics 
                (employee_id, total_commits, total_pr_merged, total_pr_conflicts, 
                 teams_responsiveness_score, performance_score, bonus_amount, last_updated)
                VALUES (%s, %s, %s, %s, %s, %s, %s, NOW())
            """, (employee_id, commits, pr_merged, pr_conflicts, teams_score, perf_score, bonus))

        conn.commit()
        print(f"[DB] Updated performance for employee {employee_id}: Score={perf_score}, Bonus=${bonus}")

    except Exception as e:
        print(f"[DB ERROR] {e}")
    finally:
        if conn:
            conn.close()


def get_current_metrics(employee_id):
    """
    Fetches current metrics for an employee to accumulate counts.
    """
    conn = None
    try:
        conn = psycopg2.connect(
            host=DB_HOST, port=DB_PORT, database=DB_NAME,
            user=DB_USER, password=DB_PASSWORD
        )
        cursor = conn.cursor()
        cursor.execute("""
            SELECT total_commits, total_pr_merged, total_pr_conflicts, teams_responsiveness_score
            FROM performance_metrics WHERE employee_id = %s
        """, (employee_id,))
        row = cursor.fetchone()
        if row:
            return {"commits": row[0], "pr_merged": row[1], "pr_conflicts": row[2], "teams_score": row[3]}
        return {"commits": 0, "pr_merged": 0, "pr_conflicts": 0, "teams_score": 0}
    except Exception as e:
        print(f"[DB ERROR] {e}")
        return {"commits": 0, "pr_merged": 0, "pr_conflicts": 0, "teams_score": 0}
    finally:
        if conn:
            conn.close()
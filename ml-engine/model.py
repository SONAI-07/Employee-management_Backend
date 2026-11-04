import torch
import torch.nn as nn


class PerformanceModel(nn.Module):
    """
    A simple feedforward neural network that takes activity metrics
    and outputs a performance score (0-100).

    Input features: [commits, pr_merged, pr_conflicts, teams_responsiveness]
    Output: performance_score (0-100)
    """

    def __init__(self):
        super(PerformanceModel, self).__init__()
        self.network = nn.Sequential(
            nn.Linear(4, 16),
            nn.ReLU(),
            nn.Linear(16, 8),
            nn.ReLU(),
            nn.Linear(8, 1),
            nn.Sigmoid()  # Output between 0 and 1
        )

    def forward(self, x):
        return self.network(x) * 100  # Scale to 0-100


def calculate_performance(commits, pr_merged, pr_conflicts, teams_score):
    """
    Runs the PyTorch model and returns a performance score and bonus.
    """
    model = PerformanceModel()
    model.eval()

    # Normalize inputs (simple normalization)
    input_tensor = torch.tensor([
        min(commits / 100.0, 1.0),  # Normalize commits
        min(pr_merged / 50.0, 1.0),  # Normalize PRs
        min(pr_conflicts / 10.0, 1.0),  # Normalize conflicts
        min(teams_score / 100.0, 1.0)  # Normalize teams score
    ], dtype=torch.float32)

    with torch.no_grad():
        score = model(input_tensor).item()

    # Calculate bonus based on score (e.g., up to $5000)
    bonus = round(score * 50, 2)

    return round(score, 2), bonus
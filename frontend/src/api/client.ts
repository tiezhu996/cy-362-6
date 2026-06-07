import { API_BASE_URL } from "../constants/app";
import type {
  OverviewResponse,
  Script,
  GroupSession,
  Review,
  ReviewRequest,
  ScriptReviewsResponse,
} from "../types";

export async function fetchOverview(): Promise<OverviewResponse> {
  const response = await fetch(`${API_BASE_URL}/overview`, {
    headers: { Accept: "application/json" },
  });

  if (!response.ok) {
    throw new Error(`Overview request failed: ${response.status}`);
  }

  return response.json() as Promise<OverviewResponse>;
}

export async function fetchScripts(): Promise<Script[]> {
  const response = await fetch(`${API_BASE_URL}/scripts`, {
    headers: { Accept: "application/json" },
  });

  if (!response.ok) {
    throw new Error(`Scripts request failed: ${response.status}`);
  }

  return response.json() as Promise<Script[]>;
}

export async function fetchScript(id: number): Promise<Script> {
  const response = await fetch(`${API_BASE_URL}/scripts/${id}`, {
    headers: { Accept: "application/json" },
  });

  if (!response.ok) {
    throw new Error(`Script request failed: ${response.status}`);
  }

  return response.json() as Promise<Script>;
}

export async function fetchGroupSessions(playerName = ""): Promise<GroupSession[]> {
  const url = playerName
    ? `${API_BASE_URL}/group-sessions?playerName=${encodeURIComponent(playerName)}`
    : `${API_BASE_URL}/group-sessions`;
  const response = await fetch(url, {
    headers: { Accept: "application/json" },
  });

  if (!response.ok) {
    throw new Error(`Group sessions request failed: ${response.status}`);
  }

  return response.json() as Promise<GroupSession[]>;
}

export async function fetchReviewsByScript(scriptId: number): Promise<ScriptReviewsResponse> {
  const response = await fetch(`${API_BASE_URL}/reviews/script/${scriptId}`, {
    headers: { Accept: "application/json" },
  });

  if (!response.ok) {
    throw new Error(`Reviews request failed: ${response.status}`);
  }

  return response.json() as Promise<ScriptReviewsResponse>;
}

export async function fetchReviewsByGroupSession(groupSessionId: number): Promise<Review[]> {
  const response = await fetch(`${API_BASE_URL}/reviews/group-session/${groupSessionId}`, {
    headers: { Accept: "application/json" },
  });

  if (!response.ok) {
    throw new Error(`Reviews request failed: ${response.status}`);
  }

  return response.json() as Promise<Review[]>;
}

export async function checkReviewExists(groupSessionId: number, playerName: string): Promise<boolean> {
  const response = await fetch(
    `${API_BASE_URL}/reviews/check?groupSessionId=${groupSessionId}&playerName=${encodeURIComponent(playerName)}`,
    { headers: { Accept: "application/json" } }
  );

  if (!response.ok) {
    throw new Error(`Check review request failed: ${response.status}`);
  }

  const data = (await response.json()) as { hasReviewed: boolean };
  return data.hasReviewed;
}

export async function submitReview(request: ReviewRequest): Promise<Review> {
  const response = await fetch(`${API_BASE_URL}/reviews`, {
    method: "POST",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
    },
    body: JSON.stringify(request),
  });

  if (!response.ok) {
    const error = await response.json().catch(() => ({ message: "提交失败" }));
    throw new Error((error as { message?: string }).message || "提交评价失败");
  }

  return response.json() as Promise<Review>;
}

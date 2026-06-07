export interface FeatureItem {
  id: number;
  title: string;
  description: string;
  status: string;
  metric: string;
}

export interface KpiItem {
  label: string;
  value: string;
  trend: string;
  tone: string;
}

export interface OperationRecord {
  key: string;
  name: string;
  owner: string;
  status: string;
  metric: string;
  priority: string;
}

export interface OverviewResponse {
  appName: string;
  appCode: string;
  description: string;
  features: FeatureItem[];
  kpis: KpiItem[];
  records: OperationRecord[];
}

export interface Script {
  id: number;
  name: string;
  type: string;
  difficulty: string;
  duration: number;
  playerCount: number;
  description: string;
  coverUrl: string | null;
  dmId: number;
  createdAt: string;
  avgScriptRating: number | null;
  reviewCount: number;
}

export interface Dm {
  id: number;
  name: string;
  phone: string;
  avatarUrl: string | null;
  createdAt: string;
  avgDmRating: number | null;
  reviewCount: number;
}

export interface GroupSession {
  id: number;
  scriptId: number;
  scriptName: string;
  dmId: number;
  dmName: string;
  sessionTime: string;
  playerNames: string[];
  status: string;
  createdAt: string;
  hasReviewed: boolean;
  currentPlayer: string | null;
}

export interface Review {
  id: number;
  groupSessionId: number;
  scriptId: number;
  scriptName: string;
  dmId: number;
  dmName: string;
  playerName: string;
  scriptRating: number;
  scriptComment: string;
  dmRating: number;
  dmComment: string;
  createdAt: string;
}

export interface ReviewRequest {
  groupSessionId: number;
  scriptId: number;
  dmId: number;
  playerName: string;
  scriptRating: number;
  scriptComment?: string;
  dmRating: number;
  dmComment?: string;
}

export interface ScriptReviewsResponse {
  reviews: Review[];
  avgScriptRating: number | null;
  reviewCount: number;
}

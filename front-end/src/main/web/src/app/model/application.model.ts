export class Application {
  id: number;
  type: string;
  days: number;
  status: string;
  message: string;
  date: string;
  username: String;
}

export class EvaluationRequest {
  id: number;
  status: string;
  message: string;

  constructor(id: number, status: string, message: string) {
    this.id = id;
    this.status = status;
    this.message = message;
  }
}

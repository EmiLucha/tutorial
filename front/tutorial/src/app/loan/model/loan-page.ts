import { Pageable } from "../../core/model/page/pageable";
import { Loan } from "./loan";

export class LoanPage {
    content: Loan[];
    pageable: Pageable;
    totalElements: number;
}
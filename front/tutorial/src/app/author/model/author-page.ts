import { Pageable } from "../../core/model/page/pageable";
import { Author } from "./author";

export class AuthorPage {
    content: Author[];
    pageable: Pageable;
    totalElements: number;
}
import { Client } from "../../client/model/client";
import { Game } from "../../game/model/game";

export class Loan {
    id: number;
    checkOutDate: Date;
    returnDate: Date;
    client: Client;
    game: Game
}
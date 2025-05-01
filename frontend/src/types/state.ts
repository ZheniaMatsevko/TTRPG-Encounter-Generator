import {Monster} from "./monster.ts";
import {GenerationRequestDto} from "../dto/filterDto.ts";
import {FilterOptions} from "./filter.ts";

export type State = {
    monsters: Monster[];
    playerLevels: number[];
    monsterAmount: number;
    generationRequest: GenerationRequestDto;
    filterOptions: FilterOptions;
    monsterTactics: boolean;
    monsterActivity: boolean;
    monsterLoot: boolean;
}
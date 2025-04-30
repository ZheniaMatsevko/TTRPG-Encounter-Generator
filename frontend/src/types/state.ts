import {Monster} from "./monster.ts";
import {GenerationRequestDto} from "../dto/filterDto.ts";
import {FilterOptions} from "./filter.ts";

export type State = {
    monsters: Monster[];
    playerLevels: number[];
    generationRequest: GenerationRequestDto;
    filterOptions: FilterOptions;
}
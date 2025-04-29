import {Monster} from "../types/monster.ts";
import {FilterDto} from "../types/filter.ts";

export function getAvailableValuesByField(monsters: Monster[], field: keyof Monster): FilterDto[] {
    const values = [
        ...new Set(monsters.map(monster => monster[field]))
    ];

    return values.map(x => {
        return {
            description: x
        }
    });
}
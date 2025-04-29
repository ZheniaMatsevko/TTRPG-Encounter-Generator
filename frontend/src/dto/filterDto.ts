export type GenerationRequestDto = {
    numberOfMonsters?: number;
    generateTactics?: boolean;
    generateActivities?: boolean;
    generateLoot?: boolean;
    charactersLevels?: number[];
    filters?: FilterParam[];
}

export type FilterParam = {
    propertyName: string;
    value: string;
    quantity: {
        number?: number;
        quantityRequirement: QuantityRequirement;
    }
}

export enum QuantityRequirement {
    NONE = 'NONE',
    ALL = 'ALL',
    EXACT = 'EXACT',
}


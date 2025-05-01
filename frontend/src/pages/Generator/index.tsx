import ResultsTable from "../../elements/Encounter/ResultsTable.tsx";
import FiltersSection from "../../elements/Encounter/filters/FiltersSection.tsx";
import FilterContext from "../../elements/contexts/FilterContext.ts";
import {useEffect, useState} from "react";
import {State} from "../../types/state.ts";
import axios from "axios";
import {config} from "../../config.ts";
import {FilterDto} from "../../types/filter.ts";
import {getAvailableValuesByField} from "../../util/util.ts";
import {Monster} from "../../types/monster.ts";
import {PlayersTable} from "../../elements/Encounter/PlayersTable.tsx";

export default function Generator() {

    const [state, setState] = useState<State>({
        filterOptions: {
            Tactics: [],
            Habitats: [],
            Activities: [],
            Alignment: [],
            Legendary: [],
            Lair: [],
            Tag: [],
            Spellcaster: [],
            Size: [],
            Type: [],
            Name: []
        },
        generationRequest: {

        },
        monsters: [],
        playerLevels: [],
    });

    const getState = async () => {
        const [monstersResponse, tacticsResponse, activitiesResponse] = await Promise.all([
            axios({
                url: config.apiURL+'monsters',
                method: 'get',
            }),
            axios({
                url: config.apiURL+'monsters/tactics',
                method: 'get'
            }),
            axios({
                url: config.apiURL+'monsters/tactics',
                method: 'get'
            }),
        ]);

        const habitats = [...new Set(monstersResponse.data.map((x: any) => x.habitats).flat())];


        state.monsterActivity = false;
        state.monsterLoot = false;
        state.monsterTactics = false;

        state.monsters = monstersResponse.data;
        state.filterOptions.Habitats = habitats.map(x => {
            return {
                description: x,
            }
        }) as unknown as FilterDto[];
        state.filterOptions.Tactics = tacticsResponse.data;
        state.filterOptions.Activities = activitiesResponse.data;

        state.filterOptions.Spellcaster = getAvailableValuesByField(state.monsters, 'spellcaster' as keyof Monster);
        state.filterOptions.Lair = getAvailableValuesByField(state.monsters, 'lair' as keyof Monster);
        state.filterOptions.Legendary = getAvailableValuesByField(state.monsters, 'legendary' as keyof Monster);
        state.filterOptions.Alignment = getAvailableValuesByField(state.monsters, 'alignment' as keyof Monster);
        state.filterOptions.Tag = getAvailableValuesByField(state.monsters, 'tag' as keyof Monster);
        state.filterOptions.Size = getAvailableValuesByField(state.monsters, 'size' as keyof Monster);
        state.filterOptions.Type = getAvailableValuesByField(state.monsters, 'type' as keyof Monster);
        state.filterOptions.Name = getAvailableValuesByField(state.monsters, 'name' as keyof Monster);
        return state;
    }


    useEffect(() => {
        getState().then(res => setState(res));
    }, []);

    return (
        <div className="flex flex-row w-full h-92">
            <FilterContext.Provider value={{state, setState}}>
                {/* section for player characters, and generation settins */}
                <div className="w-1/4 border-r-1">
                    <PlayersTable/>
                </div>

                {/* section for filters choose */}
                <div className="w-1/4 border-r-1">
                    <FiltersSection/>
                </div>



                {/* generation main screen */}
                <div className="w-1/2">
                    <ResultsTable/>
                </div>
            </FilterContext.Provider>

        </div>
    );
}
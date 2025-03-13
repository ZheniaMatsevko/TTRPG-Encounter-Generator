import FiltersSection from "../../elements/Encounter/FiltersSection.tsx";
import FiltersSection from "../../elements/Encounter/ResultsTable.tsx";

export default function Generator() {
    return (
        <div className="flex flex-row w-full h-92">
            {/* section for player characters, and generation settins */}
            <div className="w-1/4 border-r-1">
                <div className="player-adder-container">
                    <h2>Player Characters</h2>
                    {/*example of AN instance of added player*/}
                    <div className="player-instance-container">
                        <label>Level</label>
                        <select name="player-level-select">
                        {[...Array(20).keys()].map((lvl) => (
                        <option key={lvl + 1}>{lvl + 1}</option>
                        ))}
                        </select>
                        <button>-</button>
                    </div>
                </div>
                {/*the Add character button*/}
                <div className="addchar-btn-container">
                    <button>+</button>
                    <label>Add character</label>
                </div>
                {/*generation settings*/}
                <div className="generation-settings-container">
                    <h2>Generation Settings</h2>
                    {[
                      "Monster tactics",
                      "Monster loot",
                      "Monster activities",
                    ].map((setting, index) => (
                      <div key={index} className="setting-option">
                        <input type="checkbox" />
                        <label>{setting}</label>
                      </div>
                    ))}
                </div>
            </div>

            {/* section for filters choose */}
            <div className="w-1/4 border-r-1">
                <FiltersSection/>
                {/*add filter button*/}
                <div className="addfilter-btn-container">
                   <button>+</button>
                   <label>Add filter</label>
                </div>
            </div>



            {/* generation main screen */}
            <div className="w-1/2">
                <ResultsTable/>
            </div>
        </div>
    );
}
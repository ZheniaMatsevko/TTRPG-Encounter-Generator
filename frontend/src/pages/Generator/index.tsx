import FiltersSection from "../../elements/Encounter/FiltersSection.tsx";

export default function Generator() {
    return (
        <div className="flex flex-row w-full h-92">
            {/* section for player characters, and generation settins */}
            <div className="w-1/4 border-r-1">

            </div>

            {/* section for filters choose */}
            <div className="w-1/4 border-r-1">
                <FiltersSection/>
            </div>

            {/* generation main screen */}
            <div className="w-1/2">

            </div>
        </div>
    );
}
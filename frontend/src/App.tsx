import './App.css'
import TopMenu from "./elements/TopMenu";
import {BrowserRouter as Router, Routes, Route, Navigate} from "react-router-dom";
import Settings from "./pages/Settings";
import About from "./pages/About";
import Credits from "./pages/Credits";
import Generator from "./pages/Generator";
import CustomMonster from "./pages/CustomMonster";
import { useState } from "react";
import {Filter} from "./types/filter.ts";
import FilterContext from './elements/contexts/FilterContext.ts';

function App() {
    const [filters, setFilters] = useState(new Array<Filter>);

    return (
        <>
            <Router>
                <div className="p-4 dark:bg-[#1E382A] text-[#FEF7EC] w-full h-screen">
                    <FilterContext.Provider value={{filters, setFilters}}>
                        <TopMenu/>
                        <Routes>
                            <Route path="/settings" element={<Settings />} />
                            <Route path="/about" element={<About />} />
                            <Route path="/credits" element={<Credits />} />
                            <Route path="/encounter" element={<Generator />} />
                            <Route path="/custom-monster" element={<CustomMonster />} />
                            <Route path="/*" element={<Navigate to="encounter" />}/>
                        </Routes>
                    </FilterContext.Provider>

                </div>
            </Router>
        </>
    )
}

export default App;

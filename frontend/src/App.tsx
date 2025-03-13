import './App.css'
import TopMenu from "./elements/TopMenu";
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import Settings from "./pages/Settings";
import About from "./pages/About";
import Credits from "./pages/Credits";
import Generator from "./pages/Generator";
import CustomMonster from "./pages/CustomMonster";
import NotFoundPage from "./pages/NotFound";
import { createContext, useState } from "react";
import {Filter} from "./types/filter.ts";

const FilterContext = createContext<{
    filters?: Filter[],
    setFilters?:  React.Dispatch<React.SetStateAction<Filter[]>>
}>({});

function App() {
    const [filters, setFilters] = useState(new Array<Filter>);


    return (
        <>
            <Router>
                <div className="p-4">
                    <FilterContext.Provider value={{filters, setFilters}}>
                        <TopMenu/>
                        <Routes>
                            <Route path="/settings" element={<Settings />} />
                            <Route path="/about" element={<About />} />
                            <Route path="/credits" element={<Credits />} />
                            <Route path="/encounter" element={<Generator />} />
                            <Route path="/custom-monster" element={<CustomMonster />} />
                            <Route path="/*" element={<NotFoundPage />} />
                        </Routes>
                    </FilterContext.Provider>

                </div>
            </Router>
        </>
    )
}

export default [App, FilterContext];

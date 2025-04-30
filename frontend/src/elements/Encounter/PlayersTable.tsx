import {useContext, useEffect, useState} from "react";
import FilterContext from "../contexts/FilterContext.ts";


export function PlayersTable() {
    const [players, setPlayers] = useState(new Array<number>)
    const [level, setLevel] = useState(0)
    const { state, setState } = useContext(FilterContext);

    useEffect(() => {
        console.log(state);
    })

    return (
        <div className="m-4">
            <div className="flex flex-col">
                <h3 className='text-3xl font-semibold mb-2'>Add player</h3>
                <label htmlFor="playerLevel">Level</label>
                <input
                    className="border-1 rounded-xl h-8 text-center"
                    type="number"
                    name='playerLevel'
                    value={level}
                    onChange={(e) => {
                        setLevel(parseInt(e.target.value, 10))
                    }}
                />
                <button
                    type="submit"
                    className="mt-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition duration-300 ease-in-out"
                    onClick={(e) => {
                        e.preventDefault();
                        if (level > 0) {
                            if (state && setState) {
                                state.generationRequest.charactersLevels = [...players, level]
                                setState(state)
                            }
                            setPlayers([...players, level])
                        }
                    }}
                >
                    Add
                </button>
                <button
                    className="mt-4 px-4 py-2 bg-red-500 text-white rounded hover:bg-red-700 transition duration-300 ease-in-out"
                    type="submit"
                    onClick={(e) => {
                        e.preventDefault();
                        setPlayers([])
                    }}
                >
                    Remove
                </button>
            </div>
            <div>
                {players.map((player, index) => (
                    <div className="my-2 p-2 border-1 rounded-xl" key={index}>
                        <span className="text-xl font-light">
                            Player with level: {player}
                        </span>
                    </div>
                ))}
            </div>
        </div>
    );
}
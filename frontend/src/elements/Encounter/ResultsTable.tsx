import {useContext, useState} from "react";
import FilterContext from "../contexts/FilterContext.ts";
import {FilterParam} from "../../dto/filterDto.ts";

export default function ResultsTable() {
    const { state } = useContext(FilterContext)

    const [generationResult, setGenerationResult] = useState({} as any)

    function handleSubmit() {
        console.log(state?.generationRequest);

        console.log({
            generateTactics: true,
            generateActivities: true,
            filters: state?.generationRequest.filters,
        })

        fetch('http://localhost:8090/generate', {
            method: 'POST',
            body: JSON.stringify({
                generateTactics: true,
                generateActivities: true,
                filters: state?.generationRequest.filters?.map((x): FilterParam => {
                    x.propertyName = x.propertyName.toLowerCase();
                    return x
                }),
            }),
            headers: {
                'Content-Type': 'application/json',
            }
        }).then((response) => response.json()).then((data) => {
            console.log(data)
            setGenerationResult(data)
        })
    }


    return (
        <div className="w-full p-4">
            <button
                className="px-4 py-2 py-2 bg-zinc-400 text-xl italic text-white rounded hover:bg-violet-300 transition duration-300 ease-in-out"
                onClick={(handleSubmit)}
            >
                Generate Encounter
            </button>

            <table className="min-w-full border border-gray-300 table-auto mt-4">
                <thead className="bg-gray-100">
                <tr>
                    <th className="border px-4 py-2">Name</th>
                    <th className="border px-4 py-2">Size</th>
                    <th className="border px-4 py-2">Type</th>
                    <th className="border px-4 py-2">Alignment</th>
                    <th className="border px-4 py-2">CR</th>
                    <th className="border px-4 py-2">Legendary</th>
                    <th className="border px-4 py-2">Count</th>
                </tr>
                </thead>
                <tbody>
                {generationResult?.monstersWithCounts?.map(({monster, count}) => (
                    <tr key={monster.id}>
                        <td className="border px-4 py-2">{monster.name}</td>
                        <td className="border px-4 py-2">{monster.size}</td>
                        <td className="border px-4 py-2">{monster.type}</td>
                        <td className="border px-4 py-2">{monster.alignment}</td>
                        <td className="border px-4 py-2">{monster.cr}</td>
                        <td className="border px-4 py-2">{monster.legendary ? "Yes" : "No"}</td>
                        <td className="border px-4 py-2">{count}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    )
}
import { useContext } from "react";


export default function ResultsTable() {

 const {  } = useContext()

return (

<div className="result-part">
    {/* GENERATE ENCOUNTER button*/}
    <button>Generate Encounter</button>
    {/*The statistics*/}
    <div>
      <label>List of monsters: </label>
      <label>...</label>
    </div>
    <div>
      <label>Encounter difficulty: </label>
      <label>...</label>
    </div>
    {/*The table itself*/}
    <table className="monsters-table">
      <thead>
        <tr>
          {["Name", "Size", "Type", "Habitat", "Legendary", "Lair", "Spellcaster"].map(
            (header, idx) => (
              <th key={idx} scope="col">{header}</th>
            )
          )}
        </tr>
      </thead>
      <tbody>
      {/*example info*/}
        <tr>
          <td>Monster1</td>
          <td>Medium</td>
          <td>Beast</td>
          <td>Swamp</td>
          <td>No</td>
          <td>No</td>
          <td>No</td>
        </tr>
      </tbody>
    </table>
  </div>
  )

  }
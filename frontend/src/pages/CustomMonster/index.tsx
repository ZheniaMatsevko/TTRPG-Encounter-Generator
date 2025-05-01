import React, { useState } from "react";

export default function CustomMonster() {
    const [formData, setFormData] = useState({
        name: "",
        size: "MEDIUM",
        type: "BEAST",
        tag: "HUMAN",
        alignment: "NEUTRAL",
        cr: 1.0,
        legendary: false,
        lair: false,
        spellcaster: false,
        habitats: "FOREST",
    });

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: type === "checkbox" ? checked : value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const payload = {
            ...formData,
            cr: parseFloat(formData.cr),
            habitats: formData.habitats
                .split(",")
                .map((h) => h.trim().toUpperCase()),
        };

        try {
            const response = await fetch("http://localhost:8090/monsters", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload),
            });

            if (response.ok) {
                alert("Monster created successfully!");
                setFormData({
                    name: "",
                    size: "MEDIUM",
                    type: "BEAST",
                    tag: "",
                    alignment: "NEUTRAL",
                    cr: 1.0,
                    legendary: false,
                    lair: false,
                    spellcaster: false,
                    habitats: "",
                });
            } else {
                alert("Error when creating monster.");
            }
        } catch (error) {
            console.error("Error:", error);
            alert("Can't connect to server");
        }
    };

    return (
        <div className="w-1/4 container mt-4">
            <h2 className="text-3xl font-bold mb-4">Create new monster</h2>
            <form onSubmit={handleSubmit}>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-3">
                    <label className="form-label">Name</label>
                    <input
                        type="text"
                        className="border-1 rounded-xl h-8"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-3">
                    <label className="form-label">Size</label>
                    <select
                        className="border-1 rounded-xl h-8 form-select"
                        name="size"
                        value={formData.size}
                        onChange={handleChange}
                    >
                        <option value="SMALL">SMALL</option>
                        <option value="MEDIUM">MEDIUM</option>
                        <option value="LARGE">LARGE</option>
                        <option value="HUGE">HUGE</option>
                    </select>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-3">
                    <label className="form-label">Type</label>
                    <input
                        type="text"
                        className="border-1 rounded-xl h-8 form-control"
                        name="type"
                        value={formData.type}
                        onChange={handleChange}
                    />
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-3">
                    <label className="form-label">CR</label>
                    <input
                        type="number"
                        step="0.1"
                        className="border-1 rounded-xl h-8 form-control"
                        name="cr"
                        value={formData.cr}
                        onChange={handleChange}
                    />
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 form-check mb-2">
                    <label className="form-check-label">Legendary</label>
                    <input
                    type="checkbox"
                    className="form-check-input"
                    name="legendary"
                    checked={formData.legendary}
                    onChange={handleChange}
                />

                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 form-check mb-2">
                    <label className="form-check-label">Lair</label>
                    <input
                    type="checkbox"
                    className="form-check-input"
                    name="lair"
                    checked={formData.lair}
                    onChange={handleChange}
                />

                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 form-check mb-2">
                    <label className="form-check-label">Spellcaster</label>
                    <input
                    type="checkbox"
                    className="form-check-input"
                    name="spellcaster"
                    checked={formData.spellcaster}
                    onChange={handleChange}
                />

                </div>

                <button type="submit" className="mt-4 px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 transition duration-300 ease-in-out">
                    Create
                </button>
            </form>
        </div>
    );
}
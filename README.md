# Underwater Ecosystem Simulation

*Authors:* Nicolás Alcalá Olea & Bailey Crossan

---

## 🐠 Overview

This project is an advanced "predator-prey" simulation that models a dynamic underwater ecosystem. It expands upon the basic "foxes and rabbits" concept to include seven distinct organisms: one plant (Algae) and six animal species divided into predators and prey.

The simulation features a rich set of mechanics designed to approximate a real-world environment. These include random spawning, reproduction between genders, aging, hunger, and predation. A core feature is the *day/night cycle*, which dictates organism behavior—prey are active during the day and rest at night, while predators hunt continuously. This helps naturally regulate populations and prevent any single group from dominating. The ecosystem is further influenced by a weather system and a transmissible disease mechanic.

---

## ✨ Features

### Organisms
Each species has unique behaviors, diets, and lifecycles. Their color in the simulation is designed to resemble their real-world counterparts.

#### 🌿 Plant
•⁠  ⁠*Algae:* The primary food source for all prey species. It grows at a high pace to sustain the ecosystem, though its growth rate is slowed by cold and foggy weather. Algae spreads by dropping fragments rather than breeding.

#### 🐟 Prey
•⁠  *Clownfish:* Consumed by all three predator species. It eats algae during the day and sleeps at night.
•⁠  *Parrotfish:* A food source for all predators. It has a high reproduction rate to ensure there is enough food for the predators.
•⁠  ⁠*Turtle:* Eaten by White Sharks and Killer Whales, but not Swordfish. Algae sustains it for longer than other prey. To prevent overpopulation, its reproduction rate is lower.

#### 🦈 Predators
•⁠  ⁠*Swordfish:* Hunts Parrotfish and Clownfish. It has a longer lifespan and is less susceptible to disease than prey.
•⁠  *White Shark:* A versatile predator that consumes all three prey species (Turtles, Clownfish, and Parrotfish).
•⁠  ⁠*Killer Whale:* An apex predator that hunts Turtles and White Sharks. It has the highest breeding probability among predators to compensate for its low initial spawn rate.

### Ecosystem Dynamics

*🌦️ Weather System:* The simulation includes three weather conditions—*clear, foggy, and cold*—each lasting for 20-40 steps.
*Foggy:* Reduces visibility, lowering prey's feeding efficiency and predators' hunting success.
*Cold:* Makes animals less likely to move and slows algae growth.

•⁠   ⁠*🦠 Disease Mechanism:* A disease can randomly infect any animal, helping to naturally control population sizes.
    *Transmission:* The disease spreads during reproduction to both the mate and any offspring.
    *Visual Cue:* Infected animals are colored white for easy identification.

---

## 🐞 Known Bugs and Issues

•⁠  ⁠*Weather Manager Not Resetting:* When the simulation is reset, the weather from the previous session persists instead of reverting to a default "clear" state.
•⁠  *GUI Scaling:* Resizing the application window stretches the simulation view unnaturally, distorting its proportions.

---

## 🚀 Getting Started

To run this project:

1.  Clone the repository.
2.  Open the project in your preferred Java IDE.
3.  Ensure you have the required libraries for handling graphics.
4.  Run the main ⁠ Simulator ⁠ class.

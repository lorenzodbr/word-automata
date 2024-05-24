# Word Automata

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
![Build passed](https://img.shields.io/badge/build-passed-brightgreen)
![Version](https://img.shields.io/badge/version-1.0-blue)
![Maintainability](https://img.shields.io/badge/maintainability-A-brightgreen)
![Coverage](https://img.shields.io/badge/coverage-100%25-brightgreen)

![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)
![JavaFX](https://img.shields.io/badge/javafx-%23FF0000.svg?style=for-the-badge&logo=javafx&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)


> Final project for my Software Engineering course @ UniVR &mdash; A.Y. 2023/2024. A simple documentation (written in Italian) can be found [here](https://github.com/lorenzodbr/word-automata-doc). An English version of ``javadoc`` is also included in this repository. 

## Description

Word Automata is a graphical tool that allows you to create and simulate automata that recognize words. You can add states, transitions, and set the initial and final states. The tool will then allow you to simulate the automaton on a given word, showing you the path that the automaton takes.

## Installation

Instructions will be added soon™. As of right now, you can just clone the repository and run the project with Maven, using Java $\geq$ 21.

## Dependencies

TODO

## Usage

### Adding a state

![Adding a state](res/state.gif)

> Note: you can choose if a state should be final on creation. You can also change this property later.

### Adding a transition

![Adding a transition](res/transition.gif)

### Setting the initial state

![Setting the initial state](res/initial_state.gif)

> Note: by default, the initial state is the first state you add. You can change this by right-clicking on a state and selecting "Set as initial state".

### Edit a state

![Edit a state](res/edit_state.gif)

The initial state 

> Note: in this section you can edit the state label and set it as final. You can also delete the state or add transitions bounded to it.

### Edit a transition

![Edit a transition](res/edit_transition.gif)

### Search for a word

![Search for a word](res/search.gif)

> Note: the action will show you the path that the automaton takes on the given word, highlighting the followed transitions.

### Additional features

- Since the project uses a modified version of [JavaFXSmartGraph](https://github.com/brunomnsilva/JavaFXSmartGraph), automata graphs can be arranged automatically by toggling the ``Auto-position`` menu in ``Settings``.
- You can save automata to file and open it later or on another machine.
- Actions can be performed with shortcuts. They are stated near their titles in the menu bar.
- You can enable dark mode globally (you may also argue: *why isn't it enabled by default?* and you would be probably right).
- If you need a hint on state colors' meaning, you can check them in ``Help`` > ``Legend``.

## License

This project is licensed under the GPL-3. You can find the full license [here](LICENSE).
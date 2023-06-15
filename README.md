<!-- m-doescode -->

# CCHBM - The ComputerCraft HBM Bridge Mod

## What is CCHBM?

CCHBM is an addon mod to ComputerCraft and HBM's Nuclear Tech Mod that adds many interfaces for HBM blocks
as ComputerCraft peripherals allowing you to create your own missile launch designators and reactor panels.

The mod is currently in beta and as such lacks many fun features but currently has support for remote
reactor controllers and launch pads

## What features does it add?

### Reactor controller
* Activate/deactivate rods (and read)
* Set automatic shutdown (and read)
* Change coolant compression level (and read)
* Check if linked
* Read (and max):
  * Hull heat
  * Core heat
  * Water level
  * Coolant level
  * Steam level
  * Rod level

### Launch pad

* Launch missile
* Check missile presence
* Designate position (if manual designator is inserted)
* Find designated position
* Check power level
* [NYI] Get missile id

### RBMK Console

* Query information about specific column
* Raise/lower (set percentage) a specific column's rods, a specific group of rods, or all rods

## Basic Usage

Place a computer next to a launch pad or reactor and use `peripheral.wrap()` with either `"reactor_control"` or `"launchpad"` to use the functions on it.

Peripheral API documentation can be found here: [docs/peripherals](docs/peripherals).

## Where can I suggest new features?

New features can be suggested here: [discussions/features-suggestions-ideas](https://github.com/m-doescode/cchbm/discussions/categories/features-suggestions-ideas)
<br>Bugs can be reported here: [issues/bug_report](https://github.com/m-doescode/cchbm/issues/new?assignees=m-doescode&labels=bug&template=bug_report.md&title=%5BBUG+REPORT%5D+%3C+Summary+of+bug+%3E)

# Compact Launch Pad

**Peripheral name**: `compact_launchpad`

**Methods**: `launch`, `getDesignatorPos`, `setDesignatorPos`, `isDesignatorPresent`, `power`,
`maxPower`, `isMissilePresent`, `solidFuel`, `maxSolidFuel`, `tankType`, `tankCapacity`, `tankContent`

# Methods

## `launch`

Launches the missile (if present).

**Throws if**: No missile is present, no designator is present

## `getDesignatorPos`

**Returns**: `integer xPosition`, `integer zPosition` | `nil`

Returns the designated position of the manual target designator. If no position is set, it will return nil.

**Throws if**: No designator is present

## `setDesignatorPos`

**Arguments**: `integer xPosition`, `integer zPosition`

Sets the position of the designator in the launch pad.

**⚠ WARNING**: The designator **must** be a Manual Target Designator, or it will not work.

**Throws if**: No designator is present, designator is not manual target designator

## `isDesignatorPresent`

**Returns**: `boolean isDesignatorPresent`

Returns true if there is a target designator in the target designator slot in the launch pad.

## `isManualDesignator`

**Returns**: `boolean isManualDesignator`

Returns true if target designator is a Manual Target Designator. Returns false if there is no target
designator in the target designator slot or if the target designator is not a Manual Target Designator.

## `power`

**Returns**: `integer power`

Returns the amount of power currently stored in the launch pad (in HE units).

## `maxPower`

**Returns**: `integer maxPower`

Returns the maximum power capacity of the launch pad (in HE units).
Currently, it should always return `100000` (or 100k).

## `isMissilePresent`

**Returns**: `boolean isMissilePresent`

Returns true if there is a missile in the missile slot in the launch pad.

## `solidFuel`

**Returns**: `integer solidFuel`

Returns the amount of solid fuel currently stored in the launch pad. (in L)

## `maxSolidFuel`

**Returns**: `integer maxSolidFuel`

Returns the maximum solid fuel capacity of the launch pad (in L).
Currently, it should always return `25000` (or 25k).

## `tankType`

**Arguments**: `integer tank`

**Returns**: `string tankFluidType`

Returns the name of the fluid in the specified tank by the index. Index must be between 0
and 1. If the tank is not used by the missile, returns `nil`.

## `tankCapacity`

**Arguments**: `integer tank`

**Returns**: `integer tankFluidMaxCapacity`

Returns the maximum capacity that the tank at the specified index can hold. Index must be between 0
and 1. If the tank is not used by the missile, returns `nil`.
Currently, it should always return `25000` (or 25k).

## `tankContent`

**Arguments**: `integer tank`

**Returns**: `integer tankFluidAmount`

Returns the currently stored fluid amount in the tank specified by the index. Index must be between 0
and 1. If the tank is not used by the missile, returns `nil`.
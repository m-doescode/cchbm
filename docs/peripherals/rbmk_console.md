# RBMK Console

**Peripheral name**: `rbmk_console`

**Methods**: `setRods`, `setRod`, `getColumnInfo`

## `setRods`

**Arguments**: `float level`, `integer? group`

Sets the level of all rods, or a group of rods in the console.
The level should be a float between `0.00F` and `1.00F` for 0% and 100% respectively.
The group argument can be left as nil to apply changes to all rods, or a number from the list below to apply to a specific group
| Color | Number |
|--|--|
| 🟥 Red | 0  |
| 🟨 Yellow | 1  |
| 🟩Green | 2 |
| 🟦Blue | 3  |
| 🟪Purple | 4 |

(If `nil` or `-1` is passed to group, all rods are selected instead)

## `setRod`

**Arguments**: `float level` (`integer index` | `integer x`, `integer y`)

Sets the level (see above) for a specific rod either by index or by position.
The first column on the top left corner has index 0, and the next one to the right has 1, etc. This continues until the right edge where it loops back to the second row on the left side.

For position, the leftmost RBMK column has position x=0, and the rightmost has position x=15. The topmost has position y=0, and the bottommost has position y=15.

Example: `setRod(0.5, 7, 7)` sets the rod at the dead center of the console to 50%

## `getColumnInfo`

**Arguments**: `integer index` | `integer x`, `integer y`
**Returns**: `table columnInfo` | `nil`

Gets the info for a specific RBMK column either by position or index (see above).
If no column is present at that index or position, then nil is returned instead.

Here is a list of keys that can be found for each RBMK column type:
|RBMK Column Type (column id)|Keys|
|--|--|
|All|- `columnType`: The id of the column.<br />- `heat`: The column temperature (in °C) |
|Control (`control`)|- `color`: (May be nil) The color id of the control rod (see above).<br />- `level`: The level of the control rod|
|Automatic Control (`control_auto`)|- `level`: The level of the control rod|
|Boiler (`boiler`)|- `water`: The level of water in the boiler<br />- `steam`: The level of steam in the boiler<br />- `maxWater` The maximum level of water in the boiler<br />- `maxSteam` The maximum level of steam in the boiler<br />- `type`: The compression level of steam in the boiler. May be one of `steam`, `hotsteam`, `superhotsteam`, `ultrahotsteam`|
|Fuel rod (`fuel`, `fuel_sim`)|- `enrichment`: The 'durability' of the fuel rod. Inverse of the depletion amount.<br />- `xenon`: The level of xenon poisoning in the reactor.<br />- `coreTemp`: The core temperature (in °C) of the column.<br />- `skinTemp`: The skin temperature (in °C) of the column.|
<!--**⚠ IMPORTANT NOTE**: The information above only documents keys that show up in the RBMK Console Gui. Keys that show up in the DODD are also accessible but not documented here.--> <!-- <=== This is not true, so I commented it out -->

If more information is required, please open an issue or discussion in the appropriate tab and request the information needed.

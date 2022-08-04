# Eternal Artifacts Mod

- forge 1.16.5
- commissioned by adam

All config files are in the folder `serverconfig/eternalartifacts/`. 
They are synced to the client automatically, so settings on the server and will affect all players.
Json configs will be reloaded when the `/reload` command is used.  

If a json file is not defined in `/world/serverconfig/*`, it will be copied from `/config/*` (which may be supplied by the modpack), if a file is not defined there, it will be copied from the mod jar. 
This means that to return to the default configs on a given world, you must delete the file in both of those directories, then reload the world.

## Traits Config

Each artifact has its own config json file that configures the all stats for each trait (`fishing_artifact_stats.json`). 
Each trait has its own key with the following data required, defaults are generated when you start the world.  

- `iconX` (int): the x coordinate of the icon in the upgrade gui
- `iconY` (int): the y coordinate of the icon in the upgrade gui
- `iconTexture` (string): the texture to use for gui icon. for example, `minecraft:textures/item/iron_sword.png`
- `upgradeItemCost` (list of object): each entry in the list represents the items that the player must spend to upgrade this trait to the given level. 
The keys in the object are the identifiers of the items (tags are supported, prefix with `#`) and the value of each key is the number of items required. 
- `upgradeLevelCost` (list of object): each entry in the list represents the experience levels that the player must spend to upgrade this trait to the given level.
  The keys in the object are the identifiers for experience type (for normal minecraft xp, use `vanilla`) and the value of each key is the number of levels required.
- `upgradetraitRequirements` (list of object). each entry in the list represents the traits the player must have to upgrade this trait to the given level. The keys in the object identify the trait needed and the value is the level of that trait. the artifact identifier is seperated from the trait identifier by a space. if the artifact identifier is not present, it defaults to the current artifact described by the json file. ex. "eternalartifacts:fishing eternalartifacts:strength_in_water": 2. 

Each trait also has its own variables that control how powerful each level is. 
These are a list of values, where each entry is the stat for that level. 
The valid trait keys and required additional variables for each artifact is described in their respective sections. 

## Fishing Artifact 

- Gain `eternalartifacts:fishing` xp by going fishing
- How much xp each fished item gives you is defined by `fishingxpvalues.json`. 
In this file, `values` is a map of item or tag identifiers to xp amount awarded. 
Item matches are prioritized over tags (prefix tags with `#`). The `levelRatio` is the amount of xp that translates into one spendable level. 
- The shell block animates open when you approach. right click it to open gui and upgrade your traits 

### Traits

#### Ocean Lord - strength_in_water

- damageScaleMultiplier (list of float): How much additional damage to deal when the player is in water. ex. 0.5 would mean 50% more damage
- effects (list of string): Potion effects granted by eating anything in the fishes tag. separated by spaces, effect resource location then level of effect. ex. "modid:effect_path 1 modid:other_effect 2"

#### Glub Glub! - more_air

- airAmountMultiplier (list of float): how much to multiply the player's air supply by
# Enhanced Model Provider

Enhance the vanilla ModelProvider in DataGen (actually only ItemModelGenerator).

Allowing you to freely create ItemModel & ClientItem without having to register an according item.

## How to use

In the all-in-one branch, go to copy [EnhancedModelProvider](src/main/java/com/lnatit/emp/data/EnhancedModelProvider.java) and put it anywhere in your project.

Extend EnhancedModelProvider, and override #registerModels (Not the deprecated one).

Others are all the same as [NeoForged doc](https://docs.neoforged.net/docs/resources/client/models/datagen#modelprovider).

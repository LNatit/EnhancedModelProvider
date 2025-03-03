# Enhanced Model Provider

Enhance the vanilla ModelProvider in DataGen (actually only ItemModelGenerator).

Allowing you to freely create ItemModel & ClientItem without having to register an according item.

## How to use

In the **all-in-one** branch, go to copy [EnhancedModelProvider](src/main/java/com/lnatit/emp/data/EnhancedModelProvider.java) and put it anywhere in your project.

Extend EnhancedModelProvider, and override #registerModels (Not the deprecated one).

Others are all the same as [NeoForged doc](https://docs.neoforged.net/docs/resources/client/models/datagen#modelprovider).

## Examples

```java
// In your own ModModelProvider extends EnhancedModelProvider #registerModels(BlockModelGenerators, ItemModelGenerators, ClientItemModelGenerators)
// with DeferredItem SOMEITEM
// vanilla ways
itemModels.generateFlatItem(SOME_ITEM.get(), ModelTemplates.FLAT_ITEM);

// emp ways
clientItemModels.gen().withId(SOME_ITEM).all();
// you can also call it with ResourceLocation id
clientItemModels.gen().withId(id).all();
// It accepts any type of DeferredHolder ANY_REGITSTRY
clientItemModels.gen().withId(ANY_REGITSTRY).all();

// if only clientItem or itemModel needed, you can call
clientItemModels.gen.withId(SOME_ITEM).clientItemOnly();
clientItemModels.gen.withId(SOME_ITEM).modelOnly();

// More complex definitions are supported
// with ResourceLocation modelLocation, layerLocation
clientItemModels.gen()
                .withId(SOME_ITEM)
                .withModel(modelLocation)
                .withTextureMapping(TextureMapping.layer0(layerLocation))
                .withModelTemplate(ModelTemplates.FLAT_ITEM)
                .withClientItem(
                        clientItemBuilder ->
                                clientItemBuilder.withProperties(ClientItem.Properties.DEFAULT).build()
                )
                .all();
```

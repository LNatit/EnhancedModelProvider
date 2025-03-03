# Enhanced Model Provider

Enhance the vanilla ModelProvider in DataGen (actually only ItemModelGenerator).

Allowing you to freely create ItemModel & ClientItem without having to register an according item.

To use the enhanced feature, you need to extend [EnhancedModelProvider](src/main/java/com/lnatit/emp/data/EnhancedModelProvider.java), and override #registerModels (Not the deprecated one).

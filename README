Testing different json libraries for scala:
1. Dummy - do nothing, just show test suite overhead
2. PlayJson
3. json4s with native AST-string parsing
4. json4s with jackson AST-string parsing
5. jackson witn jackson-module-scala and mesosphere/jackson-case-class-module

Hardware:
1 core of i7 2.9Ghz, memory is not limited

Results:
|        Group|           Test|  Dummy|PlayJson|jackson|json4s/jackson|json4s/native|
|-------------|---------------|-------|--------|-------|--------------|-------------|
|1. obj-to-AST|complex objects|53655.9|    46.0|  194.0|          65.6|         69.7|
|1. obj-to-AST|           maps|31753.9|   156.1|  433.4|         146.8|        151.0|
|1. obj-to-AST|  plain objects|50333.2|   352.2| 1146.8|         601.2|        694.0|
|1. obj-to-AST|      sequences|51914.6|   710.1|  767.6|         140.0|        132.1|
|2. AST-to-obj|complex objects|53169.1|    59.3|   57.5|          41.6|         41.0|
|2. AST-to-obj|           maps|53936.9|   231.6|  116.7|         109.8|        109.2|
|2. AST-to-obj|  plain objects|53660.9|   607.1|  418.0|         364.2|        433.4|
|2. AST-to-obj|      sequences|51797.8|   314.1|  213.8|         131.1|        136.2|
|       3. AST|   parse string|74522.9|    90.5|66641.1|         155.6|        127.9|
|       3. AST|      to string|73392.4|   184.0|62673.6|         188.8|         36.5|
|4. full cycle|     direct str|43760.6|    14.0|   39.0|          16.9|         15.2|
|4. full cycle|    through AST|23735.8|    14.2|   38.5|          16.6|         11.7|


Comments:
* AST - abstract syntax tree, JsValue for PlayJson, JValue for json4s
* Jackson can't create AST, so it work with string directly. Thats why "AST:parse string" and "AST:to string" is so huge, sinse they really do nothing
* full cycle:direct str means converting of ComplexEntity to string and then parse it back. Flow is ComplexEntity->string->ComplexEntity
* full cycle:through AST means converting ComplexEntity->AST->string->AST->ComplexEntity
* PlayJson can't get benefits from direct string conversion, since can't ommit AST



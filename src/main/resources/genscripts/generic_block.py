from jsongen import *

copy([
	('block_model_generic.json', 'assets/{modid}/models/block/{name}.json'),
	('block_item_generic.json', 'assets/{modid}/models/item/{name}.json'),
	('blockstate_generic.json', 'assets/{modid}/blockstates/{name}.json'),
])

localize_standard('block')
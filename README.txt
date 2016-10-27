===========
LedDisplay
===========
A javascript library to show a led display inside a div, written in Scala.js

====
API
====

/**
 * creates a new LedDisplayManager inside the given id with optional configuration
 */
LedDisplayManager(divId, LedDisplayConfig)

/**
 * adds a text to the end of the buffer to be scrolled
 */
LedDisplayManager.addText(String text)

/**
 * clears the display and sets the text to be
 */
LedDisplayManager.setText(String text)

/**
 * starts / stops scrolling
 */
LedDisplayManager.scrolling boolean

/**
 * creates a new LedDisplayConfig to customize display
 */
LedDisplayConfig()

/**
 * single cell size in pixels
 * default = 4
 */
LedDisplayConfig.cellSize Int

/**
 * margin between cells in pixels
 * default = 2
 */
LedDisplayConfig.margin Int

/**
 * width in cells
 * default = 100
 */
LedDisplayConfig.width Int

/**
 * height in cells
 * default = 14
 */
LedDisplayConfig.height Int

/**
 * color
 * default = "#ff0000"
 */
LedDisplayConfig.color String

/**
 * timeout between refresh in milliseconds
 * default = 50
 */
LedDisplayConfig.timeout Int

/**
 * fon family, use web safe fonts (http://www.cssfontstack.com/)
 * default = "Courier New"
 */
LedDisplayConfig.fontFamily String

/**
 * font size in pixels
 * default = 12
 */
LedDisplayConfig.fontSize Int

/**
 * cells are ON or OFF, with no gradients, but since fonts are web fonts are aliased, you must provide a threshold
 * to understand when component's color (0 to 255)
 * default = 50
 */
LedDisplayConfig.fontColorThreshold Int

=========
EXAMPLES
=========

Import the library
-------------------
<script type="text/javascript" src="leddisplay-opt.js"></script>


Fixed text
-----------
// 'ledDisplay' is the id of the containing div.
var dm = new LedDisplayManager('ledDisplay');
dm.setText('Fixed text');


Scrolling text
---------------
var dm = new LedDisplayManager('ledDisplay');
dm.addText('Scrolling text');
db.scrolling = true;


Custom configuration
----------------------
var config = nwe LedDisplayConfig();
config.color = '#00ff00';

var dm = new LedDisplayManager('ledDisplay', config);
dm.addText('Scrolling text');
db.scrolling = true;


========
CREDITS
========

Raster font (it's not used anymore)
------------------------------------
from https://github.com/idispatch/raster-fonts/blob/master/08x08_DOS437_unknown.png

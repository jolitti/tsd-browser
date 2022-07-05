# StartPageController 
class working as a controller for the start page, contains methods launched 
using the nodes of the view, and auxiliary ones. It also has member Objects used to
interact with the nodes. Allows for communications between the view and the model and also to make changes to the view.
---

## private Object members 
## `nationCCB`
checkComboBox used to select the nations in the filters
## `tspCCb`
checkComboBox used to select the providers in the filters
## `typeCCB`
checkComboBox used to select the types in the filters
## `statusCCb`
checkComboBox used to select the status in the filters

## `search`
button used to start the search

## `flags`
FlowPane used to contian the flags for the quick search

## `model`
static instance of the model

## `providersName`
map used to convert the providers ids into names

## `nationsName`
map used to convert the nations ids into names

## `nationId`
map used to convert the nations names into ids

## `providerId`
map used to convert the providers names into ids

## `fullfilter`
static instance of a filter containing all the data+

---

## public methods

## `initialize`
Method used to initialize the view, adds and event handler and listener to the CCBs, initializes the maps 

## `printFlags`
method used to print the flags in the start page, and adds an event handler to each flag

## `flagButton`
method launched after a flag is clicked, launches a query with only the state

## `setFilters`
method that sets the filters in the checkComboBoxes using a services filter as a data container

## `checkAll`
method used to implement the mutual exclusion of select all and a any other choice inside a filter

## `changeToSearchScene`
method used to launch the initials query, and change scene to the result page

## `getComplementaryFilters`
method used to implement the filters complementarity

## `intersections`
method that makes the intersection of the 4 Optional contained in the filters

## `inter`
method that intersects 2 lists

## `allcheck`
method that returns all the items in the ccb, to use in case of select all

## `getFilter`
method that return a serviceFilter containing the items selected in the CCBs

## `copy`
method used to create a deep copy of Observable lists

## `initFilter`
method that sets the check in the CCBs using the given checkModels
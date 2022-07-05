# ResultsPageController
class working as a controller for the results  page, contains methods launched
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

## `selectedFilters`
treeView used to print the filters of a particular query

## `serviceGP`
table view used to print the services of a particular query

## `homeButton`
button used to return to the start page

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
static instance of a filter containing all the data

---

## public methods

## `initialize`
Method used to initialize the view, adds and event handler and listener to the CCBs, initializes the maps

## `searchByFilters`
method used to execute the search, gets the services from the model

## `initFilter`
method that sets the check in the CCBs using the given checkModels

## `printFilters`
method that prints the selected filters in the threeView

## `printServices`
method used to print the services in the tableView

## `qServiceToString`
methos used to convert the qServiceType from array to string

## `setFilters`
method that sets the filters in the checkComboBoxes using a services filter as a data container

## `checkAll`
method used to implement the mutual exclusion of select all and a any other choice inside a filter

## `homeScene`
method used to return to the start page

## `getComplementaryFilters`
method used to implement the filters complementarity

## `allcheck`
method that returns all the items in the ccb, to use in case of select all

## `getFilter`
method that return a serviceFilter containing the items selected in the CCBs

## `copy`
method used to create a deep copy of Observable lists

## `intersections`
method that makes the intersection of the 4 Optional contained in the filters

## `inter`
method that intersects 2 lists




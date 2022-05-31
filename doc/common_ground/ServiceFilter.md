# ServiceFilter

Object used to interrogate the database for `Service`s 
that match a combination of 4 parameters, as required by specs.  
The parameters are encapsulated by `java.util`'s `Optional`, 
to denote that they might not be present.

---

## The parameters

The four parameters represent the four attributes the user can 
use for a trusted service query, as described in the specs.  
If a parameter is not `Optional.empty()`, it's a `Set` of either 
strings or integers, and is matched by a service's corresponding 
attribute if it (or, if it's a list, at least one element in it) 
is present in the set.

---

## The `matches` method

This method takes a `Service` as a parameter and outputs a 
boolean either if the filter is empty or all non-null 
parameters match the criteria described above.
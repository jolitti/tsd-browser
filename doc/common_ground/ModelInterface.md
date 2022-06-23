# ModelInterface

Objects that implement this interface are declaring themselves 
to be a database that holds a query-able list of `Service`s

---

The four methods that must be implemented are:

## `getCountryCodeToNames`

Must return a `Map<String,String>` that pairs country codes
("FR") to actual country names ("France").

## `getCodeToProviderNames`

Must return a `Map<Integer,String>` that pairs provider IDs
(0) to actual service provider names 
("A-Trust Gesellschaft für Sicherheitssysteme im 
elektronischen Datenverkehr GmbH").

## `getComplementaryFilter`

This is probably the most difficult method to describe 
in the entire documentation.  
Supposing a valid, partially completed, `ServiceFilter`, 
only a subset of the possible completed filters actually produce 
results. The specs ask the program to display only non-empty filter 
selections, so this method is used to "fill in" an incomplete 
filter with valid parameters.  
The non-empty parameters remain unmodified, as adding anything to 
them cannot invalidate a previously valid selection.

## `getServices`

Get all the services that match a certain `ServiceFilter`
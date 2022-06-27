# Service

The building block of the entire application, used to express the elements of the 
[EU trusted service database](https://esignature.ec.europa.eu/efda/tl-browser/#/screen/home).

Each object of this class represents a single EU Trusted Service 
(from esignatures to certified email) and its various pieces of metadata.

---

The four most important attributes of the class are:

## `tspId`

An integer that's unique to each provider. 
Translated to an actual name via the `Map<Integer,String>` 
obtained via `ModelInterface`'s `getCodeToProviderNames`

## `countryCode`

Two-character uppercase abbreviation of the service's country
(ex. "France" to "FR"). 
Translated to the country's full name via the `Map<String,String>` 
obtained via `ModelInterface`'s `getCountryCodeToNames`

## `currentStatus`

String equal to a link to the definition of its current status
(e.g. active/withdrawn/...)

## `qServiceTypes`

A `List` of the `String`s representing the various service types 
this particular service fulfills
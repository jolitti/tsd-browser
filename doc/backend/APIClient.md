# ApiClient

This is a utility static class that encapsulates dialogue with 
the trusted service API. Documentation for the API itself 
is found at 
[this page](https://esignature.ec.europa.eu/efda/swagger-ui.html#/).  

This class was written as a first experience with JSON and HTTP 
requests, so it may contain inefficient or unsafe code.

---

## Public methods

### `getData`

This one reads the JSON at the given url, which is expected to 
be an array of providers, and, while saving an association between 
provider id and name, flattens it into a list of `Service`s.  
The resulting two datasets are returned via a `DataHolder`.

### `getCountryMap`

The second public function reads a second URL that contains 
pairs of country codes (two character uppercase strings) 
and corresponding full country names, and returns them as a 
`Map`

---

## Private methods

### `readJsonFromUrl`

This is used internally to perform the actual HTTP request. 
Both public methods only request JSON arrays, so we can specify the 
return type as a JSON array itself.

### `buildServiceFromJSONObject`

This is a utility function that converts a suitable JSON 
object into a `Service` record. We prefer to use a strictly 
typed object instead of a more flexible JSONObject because it 
simplifies reading downstream (code that uses this service) 
doesn't need to ensure that the field is actually there.
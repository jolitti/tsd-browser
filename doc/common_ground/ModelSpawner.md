# Model Spawner

The point of contact between the gui framework and the "backend" database.  
It's a simple pseudo-static class that can be used to obtain a `ServiceDatabase`
(presented behind the interface `ModelInterface`)

## `getModelInstance`

Static method that calls `APIClient` to interrogate the trusted service provider API 
(<https://esignature.ec.europa.eu/efda/swagger-ui.html#/>), 
obtains the two translation `Map`s and the `List` of `Service`s and builds the 
`ServiceDatabase`
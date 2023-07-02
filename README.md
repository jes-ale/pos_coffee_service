# pos_coffee_service
Open source JVM web service that makes use of the apache XML-RPC client to make calls into an odoo 16 instance.
## Features:
### - In-memory PoS Order (pos.order) queue to feed multiple PoS sessions and collect from multiple PoS sessions.
### - In-memory Manufacturing Order (mrp.production) read-only queue to sync with external frotend.
### - Insert into pos.order queue sets the order for mrp.production queue to be displayed in (or fetched from) the external frontend.
Odoo module and external frontend tools are not open source given the use case providen by bussines rules. This may change in the future.

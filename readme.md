# setup

* install postgres

```
brew install postgres
psql -d postgres
create database cloudplay;
create user cloudplay with password 'cloudplay';
GRANT ALL PRIVILEGES ON DATABASE cloudplay to cloudplay;
```


db.userInterestStocks.createIndex( { "userId": 1 }, { unique: true } );
db.user.createIndex( { "userName": 1 }, { unique: true } );
db.stock.createIndex({"code": 1},{unique: true});
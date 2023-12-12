from flask import Flask
from flask_cors import CORS
from flask_sqlalchemy import SQLAlchemy


app = Flask(__name__)
CORS(app)

# jangan lupa diganti format mysqlnya
# Format: "mysql+pymysql://username:password@host:port/database_name"
app.config["SQLALCHEMY_DATABASE_URI"] = "mysql+pymysql://root:mysql@localhost:3306/haire"
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
app.config["SQLALCHEMY_ECHO"] = True
db = SQLAlchemy(app)
app.app_context().push()


@app.route("/")
def hello():
    return "Hello World!"

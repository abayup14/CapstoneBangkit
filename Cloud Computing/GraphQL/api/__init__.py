from flask import Flask
from flask_cors import CORS
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
CORS(app)
''''
# Import routes di sini setelah app diinisialisasi
from app import routes  
'''
app.config["SQLALCHEMY_DATABASE_URI"] = "mysql+pymysql://root:mysql@localhost:3306/hAIre"
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
db = SQLAlchemy(app)
app.app_context().push()

@app.route("/")
def hello():
    return "Hello World!"

if __name__ == "__main__":
    app.run(debug=True)


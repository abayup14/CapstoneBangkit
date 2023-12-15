from flask import Flask
from flask_cors import CORS
from flask_sqlalchemy import SQLAlchemy
import os

app = Flask(__name__)
CORS(app)

# Configure your Google Cloud SQL connection
# Replace 'your-connection-parameters' with your actual MySQL connection details
google_cloud_sql_connection_params = {
    'user': 'root',
    'password': 'root',
    'host': '34.128.118.209',
    'database': 'haire',
}

# Configure SQLAlchemy to use Google Cloud SQL
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://%(user)s:%(password)s@%(host)s/%(database)s' % google_cloud_sql_connection_params
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

# Create a SQLAlchemy instance
db = SQLAlchemy(app)
app.app_context().push()

'''''
# Import routes after initializing the app and configuring the database
from app import routes'''

# Simple route for testing
@app.route("/")
def hello():
    return "Hello World!"

if __name__ == '__main__':
    app.run(debug=True)





'''''
from flask import Flask
from flask_cors import CORS
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
CORS(app)

'
# Import routes di sini setelah app diinisialisasi
from app import routes
''

app.config["SQLALCHEMY_DATABASE_URI"] = "mysql+pymysql://root:mysql@mysql:3306/haire"
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
db = SQLAlchemy(app)
app.app_context().push()

@app.route("/")
def hello():
    return "Hello World!"

if __name__ == "__main__":
    app.run(debug=True)

'''

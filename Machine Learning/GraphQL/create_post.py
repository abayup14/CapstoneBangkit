from datetime import datetime
from api.models import Post
from app import db
from api import app


'''with app.app_context():
    db.create_all()'''

with app.app_context():
    current_date = datetime.today().date()
    new_post = Post(title="My first post",
                    description="Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    created_at=current_date)
    db.session.add(new_post)
    db.session.commit()

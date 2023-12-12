# mutations.py
from datetime import datetime
from ariadne import convert_kwargs_to_snake_case
from api import db
from api.models import User, Company, Lowongan, Skills, UserHasSkills, SkillsDibutuhkan, Apply, Notifikasi

@convert_kwargs_to_snake_case
def create_user_resolver(obj, info, input):
    try:
        user_data = input.get('user')
        user = User(**user_data)
        db.session.add(user)
        db.session.commit()
        payload = {
            "success": True,
            "user": user.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload

@convert_kwargs_to_snake_case
def create_company_resolver(obj, info, input):
    try:
        company_data = input.get('company')
        company = Company(**company_data)
        db.session.add(company)
        db.session.commit()
        payload = {
            "success": True,
            "company": company.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload

@convert_kwargs_to_snake_case
def create_lowongan_resolver(obj, info, input):
    try:
        lowongan_data = input.get('lowongan')
        lowongan = Lowongan(**lowongan_data)
        db.session.add(lowongan)
        db.session.commit()
        payload = {
            "success": True,
            "lowongan": lowongan.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload

@convert_kwargs_to_snake_case
def create_skills_resolver(obj, info, input):
    try:
        skills_data = input.get('skills')
        skills = Skills(**skills_data)
        db.session.add(skills)
        db.session.commit()
        payload = {
            "success": True,
            "skills": skills.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload

@convert_kwargs_to_snake_case
def create_user_has_skills_resolver(obj, info, input):
    try:
        user_has_skills_data = input.get('user_has_skills')
        user_has_skills = UserHasSkills(**user_has_skills_data)
        db.session.add(user_has_skills)
        db.session.commit()
        payload = {
            "success": True,
            "user_has_skills": user_has_skills.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload

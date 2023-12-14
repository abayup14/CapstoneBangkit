# mutations.py
from datetime import datetime
from ariadne import convert_kwargs_to_snake_case
from api.main import db
from api.models import User, Company, Lowongan, Skills, UserHasSkills, SkillsDibutuhkan, Apply, Notifikasi


@convert_kwargs_to_snake_case
def create_user_resolver(obj, info, nama, email, password, nomor_telepon, tgl_lahir, nik, pengalaman, pengalaman_pro,
                         edukasi, url_photo, deskripsi, stream):
    try:
        new_user = User(
            nama=nama,
            email=email,
            password=password,
            nomor_telepon=nomor_telepon,
            tgl_lahir=tgl_lahir,
            nik=nik,
            pengalaman=pengalaman,
            pengalaman_pro=pengalaman_pro,
            edukasi=edukasi,
            url_photo=url_photo,
            deskripsi=deskripsi,
            stream=stream
        )
        #user_data = input.get('user')
        #user = User(**user_data)
        db.session.add(new_user)
        db.session.commit()
        payload = {
            "success": True,
            "user": new_user.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload


@convert_kwargs_to_snake_case
def create_company_resolver(obj, info, nama, alamat, email, password, url_photo, deskripsi):
    try:
        new_company = Company(
            nama=nama,
            alamat=alamat,
            email=email,
            password=password,
            url_photo=url_photo,
            deskripsi=deskripsi
        )
        #company_data = input.get('company')
        #company = Company(**company_data)
        db.session.add(new_company)
        db.session.commit()
        payload = {
            "success": True,
            "company": new_company.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload


@convert_kwargs_to_snake_case
def create_lowongan_resolver(obj, info, nama, deskripsi, jmlh_butuh, company_id, url_photo):
    try:
        new_lowongan = Lowongan(
            nama=nama,
            deskripsi=deskripsi,
            jmlh_butuh=jmlh_butuh,
            company_id=company_id,
            url_photo=url_photo
        )
        #lowongan_data = input.get('lowongan')
        #lowongan = Lowongan(**lowongan_data)
        db.session.add(new_lowongan)
        db.session.commit()
        payload = {
            "success": True,
            "lowongan": new_lowongan.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload


@convert_kwargs_to_snake_case
def create_skills_resolver(obj, info, nama):
    try:
        new_skills = Skills(nama=nama)
        #skills_data = input.get('skills')
        #skills = Skills(**skills_data)
        db.session.add(new_skills)
        db.session.commit()
        payload = {
            "success": True,
            "skills": new_skills.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload


@convert_kwargs_to_snake_case
def create_user_has_skills_resolver(obj, info, iduser, idskills):
    try:
        new_uhs = UserHasSkills(iduser=iduser, idskills=idskills)
        #user_has_skills_data = input.get('user_has_skills')
        #user_has_skills = UserHasSkills(**user_has_skills_data)
        db.session.add(new_uhs)
        db.session.commit()
        payload = {
            "success": True,
            "user_has_skills": new_uhs.to_dict()
        }
    except ValueError:
        payload = {
            "success": False,
            "errors": ["Incorrect data provided."]
        }
    return payload

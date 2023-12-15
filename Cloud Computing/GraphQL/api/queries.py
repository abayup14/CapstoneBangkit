from api.models import Notifikasi, User, Company, Lowongan, Skills, UserHasSkills, Edukasi, Pengalaman, Skills, Apply
from ariadne import convert_kwargs_to_snake_case


@convert_kwargs_to_snake_case
def cek_login_user(obj, info, email, password):
    try:
        user = User.query.filter_by(email=email, password=password).first()
        payload = {
            "success": True,
            "user": user.to_dict()
        }
    except AttributeError:
        payload = {
            "success": False,
            "errors": [f"User not found"]
        }
    return payload

@convert_kwargs_to_snake_case
def cek_login_company(obj, info, email, password):
    try:
        company = Company.query.filter_by(email=email, password=password).first()
        payload = {
            "success": True,
            "company": company.to_dict()
        }
    except AttributeError:
        payload = {
            "success": False,
            "errors": [f"Company not found"]
        }
    return payload

@convert_kwargs_to_snake_case
def list_users_resolver(obj, info):
    try:
        users = [user.to_dict() for user in User.query.all()]
        payload = {
            "success": True,
            "users": users
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

'''
@convert_kwargs_to_snake_case
def list_companies_resolver(obj, info):
    try:
        companies = [company.to_dict() for company in Company.query.all()]
        payload = {
            "success": True,
            "companies": companies
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

@convert_kwargs_to_snake_case
def list_lowongans_resolver(obj, info):
    try:
        lowongans = [lowongan.to_dict() for lowongan in Lowongan.query.all()]
        payload = {
            "success": True,
            "lowongans": lowongans
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

@convert_kwargs_to_snake_case
def list_skills_resolver(obj, info):
    try:
        skills = [skill.to_dict() for skill in Skills.query.all()]
        payload = {
            "success": True,
            "skills": skills
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

@convert_kwargs_to_snake_case
def list_user_has_skills_resolver(obj, info):
    try:
        user_has_skills = [uhs.to_dict() for uhs in UserHasSkills.query.all()]
        payload = {
            "success": True,
            "user_has_skills": user_has_skills
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload
'''

# @convert_kwargs_to_snake_case
# def list_posts_resolver(obj, info):
#     try:
#         posts = [post.to_dict() for post in Post.query.filter(Post.description.like("%%")).all()]
#         print(posts)
#         payload = {
#             "success": True,
#             "posts": posts
#         }
#     except Exception as e:
#         payload = {
#             "success": False,
#             "errors": [str(e)]
#         }
#     return payload

# @convert_kwargs_to_snake_case
# def list_users_resolver(obj, info):
#     try:
#         users = [user.to_dict() for user in User.query.all()]
#         print(users)
#         payload = {
#             users
#         }
#     except Exception as e:
#         payload = {
#             "success": False,
#             "errors": [str(e)]
#         }
#     return payload


# @convert_kwargs_to_snake_case
# def get_post_resolver(obj, info, id):
#     try:
#         post = Post.query.get(id)
#         payload = {   
#             "success": True,
#             "post": post.to_dict()
#         }
#     except AttributeError:
#         payload = {
#             "success": False,
#             "errors": [f"Post item matching {id} not found"]
#         }
#     return payload

@convert_kwargs_to_snake_case
def list_companies_resolver(obj, info):
    try:
        companies = [company.to_dict() for company in Company.query.all()]
        payload = {
            "success": True,
            "companies": companies
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

@convert_kwargs_to_snake_case
def list_lowongans_resolver(obj, info):
    try:
        lowongans = [lowongan.to_dict() for lowongan in Lowongan.query.all()]
        payload = {
            "success": True,
            "lowongans": lowongans
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

@convert_kwargs_to_snake_case
def list_skills_resolver(obj, info):
    try:
        skills = [skill.to_dict() for skill in Skills.query.all()]
        payload = {
            "success": True,
            "skills": skills
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

# @convert_kwargs_to_snake_case
# def list_user_has_skills_resolver(obj, info):
#     try:
#         user_has_skills = [uhs.to_dict() for uhs in UserHasSkills.query.all()]
#         payload = {
#             "success": True,
#             "user_has_skills": user_has_skills
#         }
#     except Exception as e:
#         payload = {
#             "success": False,
#             "errors": [str(e)]
#         }
#     return payload


@convert_kwargs_to_snake_case
def list_edukasi_user_resolver(obj, info, user_iduser):
    try:
        edukasi = [edukasi.to_dict() for edukasi in Edukasi.query.filter_by(user_iduser=user_iduser)]
        payload = {
            "success": True,
            "edukasi": edukasi
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload


@convert_kwargs_to_snake_case
def list_pengalaman_user_resolver(obj, info, user_iduser):
    try:
        pengalaman = [pengalaman.to_dict() for pengalaman in Pengalaman.query.filter_by(user_iduser=user_iduser)]
        payload = {
            "success": True,
            "pengalaman": pengalaman
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

@convert_kwargs_to_snake_case
def list_user_has_skills_resolver(obj, info, user_iduser):
    try:
        skills = [skills.to_dict() for skills in Skills.query.join(UserHasSkills).filter(UserHasSkills.user_iduser == user_iduser).all()]
        payload = {
            "success": True,
            "skills": skills
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

@convert_kwargs_to_snake_case
def list_lowongans_company_resolver(obj, info, company_id):
    try:
        lowongans = [lowongan.to_dict() for lowongan in Lowongan.query.filter(Lowongan.company_id == company_id).all()]
        payload = {
            "success": True,
            "lowongan": lowongans
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

@convert_kwargs_to_snake_case
def list_lowongans_user_search_resolver(obj, info, search):
    try:
        lowongans = [lowongan.to_dict() for lowongan in Lowongan.query.filter(Lowongan.nama.like(f'%{search}%')).all()]
        payload = {
            "success": True,
            "lowongan": lowongans
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

@convert_kwargs_to_snake_case
def list_lowongans_user_apply_resolver(obj, info, user_iduser):
    try:
        lowongans = [lowongan.to_dict() for lowongan in Lowongan.query.join(Apply).filter(Apply.user_iduser == user_iduser).all()]
        payload = {
            "success": True,
            "lowongan": lowongans
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

@convert_kwargs_to_snake_case
def list_notifikasi_resolver(obj, info, user_iduser):
    try:
        notifikasis = [notifikasi.to_dict() for notifikasi in Notifikasi.query.filter(Notifikasi.user_iduser == user_iduser).all()]
        payload = {
            "success": True,
            "notifikasi": notifikasis
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

@convert_kwargs_to_snake_case
def list_apply_resolver(obj, info, lowongan_id):
    try:
        applies = [apply.to_dict() for apply in Apply.query.filter(Apply.lowongan_id == lowongan_id).all()]
        payload = {
            "success": True,
            "apply": applies
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload
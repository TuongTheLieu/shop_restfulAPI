import 'dart:convert';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'package:jwt_decoder/jwt_decoder.dart';
import 'package:image_picker/image_picker.dart';

class ProfileScreen extends StatefulWidget {
  const ProfileScreen({super.key});

  @override
  _ProfileScreenState createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  String api = "localhost";
  String fullName = '';
  String phoneNumber = '';
  String address = '';
  String dateOfBirth = '';
  String avatarUrl = '';
  bool isLoading = true;
  bool isError = false;
  int? userId;
  File? _imageFile;

  Future<void> _fetchUserProfile() async {
    final prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('auth_token');

    if (token == null) {
      setState(() {
        isLoading = false;
        isError = true;
      });
      return;
    }

    final decodedToken = JwtDecoder.decode(token);
    setState(() {
      userId = decodedToken['id'];
    });

    if (userId == null) {
      setState(() {
        isLoading = false;
        isError = true;
      });
      return;
    }

    final response = await http.get(
      Uri.parse("http://$api:8088/api/v1/users/$userId"),
      headers: {
        'Authorization': 'Bearer $token',
      },
    );

    if (response.statusCode == 200) {
      final Map<String, dynamic> responseData = jsonDecode(response.body);

      setState(() {
        fullName = responseData['fullName'] ?? 'N/A';
        phoneNumber = responseData['phoneNumber'] ?? 'N/A';
        address = responseData['address'] ?? 'N/A';
        dateOfBirth = responseData['dateOfBirth'] ?? 'N/A';
      });

      await _fetchAvatar(); // Gọi API lấy ảnh đại diện
    } else {
      setState(() {
        isLoading = false;
        isError = true;
      });
    }
  }

  Future<void> _fetchAvatar() async {
    final prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('auth_token');

    if (token == null || userId == null) {
      setState(() {
        isLoading = false;
        isError = true;
      });
      return;
    }

    final response = await http.get(
      Uri.parse("http://$api:8088/api/v1/avatar/$userId"),
      headers: {'Authorization': 'Bearer $token'},
    );

    if (response.statusCode == 200) {
      final Map<String, dynamic> responseData = jsonDecode(response.body);
      setState(() {
        avatarUrl = responseData['avatar_url'] ?? '';
        isLoading = false; // Dừng xoay tròn
      });
    } else {
      setState(() {
        isLoading = false;
        isError = true;
      });
    }
  }


  Future<void> _pickImage() async {
    final picker = ImagePicker();
    final pickedFile = await picker.pickImage(source: ImageSource.gallery);

    if (pickedFile != null) {
      setState(() {
        _imageFile = File(pickedFile.path);
      });
    }
  }

  Future<void> _uploadAvatar() async {
    if (_imageFile == null || userId == null) {
      return;
    }

    final prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('auth_token');
    if (token == null) {
      setState(() {
        isError = true;
        return;
      });
    }

    final request = http.MultipartRequest(
      'POST',
      Uri.parse("http://$api:8088/api/v1/avatar/$userId"),
    );
    request.headers['Authorization'] = 'Bearer $token';
    request.files.add(await http.MultipartFile.fromPath('file', _imageFile!.path));

    final response = await request.send();

    if (response.statusCode == 200) {
      await _fetchAvatar(); // Cập nhật lại ảnh đại diện sau khi upload thành công
    } else {
      setState(() {
        isError = true;
      });
    }
  }

  @override
  void initState() {
    super.initState();
    _fetchUserProfile();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Profile')),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : isError
          ? const Center(child: Text('Failed to load profile.'))
          : Padding(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const SizedBox(height: 20),
            const Text(
              "Profile Information",
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 22),
            ),
            const SizedBox(height: 20),
            Center(
              child: CircleAvatar(
                radius: 60,
                backgroundImage: avatarUrl.isNotEmpty
                    ? NetworkImage(avatarUrl)
                    : const AssetImage('assets/avatar_default.png')
                as ImageProvider,
              ),
            ),
            const SizedBox(height: 20),
            Text('Full Name: $fullName', style: const TextStyle(fontSize: 18)),
            const SizedBox(height: 10),
            Text('Phone Number: $phoneNumber', style: const TextStyle(fontSize: 18)),
            const SizedBox(height: 10),
            Text('Address: $address', style: const TextStyle(fontSize: 18)),
            const SizedBox(height: 10),
            Text('Date of Birth: $dateOfBirth', style: const TextStyle(fontSize: 18)),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: _pickImage,
              child: const Text('Choose Avatar'),
            ),
            const SizedBox(height: 10),
            ElevatedButton(
              onPressed: _uploadAvatar,
              child: const Text('Upload Avatar'),
            ),
          ],
        ),
      ),
    );
  }
}


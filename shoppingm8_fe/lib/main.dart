import 'package:flutter/material.dart';
import 'package:shoppingm8_fe/auth/registrationWidget.dart';
import 'package:shoppingm8_fe/menu/mainMenuWidget.dart';

import 'auth/loginWidget.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'ShoppingM8',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.green,
        backgroundColor: Color(0xFFDDFFEE),
        cardColor: Colors.white,
        accentColor: Colors.lightGreenAccent,
        buttonTheme: ButtonThemeData(
          buttonColor: Colors.lightGreen,
          textTheme: ButtonTextTheme.normal,
          padding: EdgeInsets.all(5),
        ),
      ),
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key}) : super(key: key);

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {

  @override
  Widget build(BuildContext context) {
    return MainMenuWidget(
      logout: () => print("logout"),
      moveToAccountManagement: () => print("moveToAccountManagement"),
      moveToFriendList: () => print("moveToFriendList"),
      moveToListScreen: () => print("moveToListScreen"),);
  }
}

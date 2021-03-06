import 'package:dotted_border/dotted_border.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:shoppingm8_fe/common/roundButtonWidget.dart';
import 'package:shoppingm8_fe/list/invitation/listInvitationsWidget.dart';

class InvitationsTileWidget extends StatelessWidget {
  final Function addToListsFunction;

  const InvitationsTileWidget({Key key, this.addToListsFunction}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      height: double.infinity,
      width: double.infinity,
      padding: EdgeInsets.only(bottom: 100, top: 50, left: 50, right: 50),
      child: DottedBorder(
        color: Colors.grey,
        dashPattern: [10, 4],
        strokeWidth: 1,
        child: Card(
          color: Color.fromARGB(12, 0, 100, 100),
          elevation: 0,
          margin: EdgeInsets.all(0),
          child: Padding(
            padding: EdgeInsets.symmetric(horizontal: 25, vertical: 10),
            child: Row(
              mainAxisSize: MainAxisSize.max,
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                Column(
                  mainAxisSize: MainAxisSize.max,
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: <Widget>[
                    Padding(
                      padding: EdgeInsets.symmetric(horizontal: 15, vertical: 10),
                      child:
                      Text("Click to see \ninvitations",
                          style: TextStyle(
                              fontSize: MediaQuery.of(context).size.width / 15
                          ),
                        textAlign: TextAlign.center,
                      ),
                    ),
                    Padding(
                      padding: EdgeInsets.all(10),
                      child: RoundButtonWidget(
                        icon: Icons.group_add,
                        radius: 40,
                        color: Colors.lightBlueAccent,
                        onPressed: () => Navigator.push(context, MaterialPageRoute(builder: (context) => ListInvitationsWidget())),
                      ),
                    )
                  ],
                ),
              ],
            )
          ),
        ),
      )
    );
  }

}

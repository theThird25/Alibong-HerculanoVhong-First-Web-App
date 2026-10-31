import type { FormEvent } from "react";
import { useState } from "react";
import api, { getErrorMessage } from "../api/client";
import { getUser, saveUser } from "../auth/session";
import {
    Button, Card, ErrorText, Label, MutedText, PageLink, SuccessText, TextInput,
} from "../components/ui";
import type { User } from "../types";

interface Message {
  text: string;
  ok: boolean;
}

export default function Profile() {
  const [fullName, setFullName] = useState<string>(getUser()?.fullName ?? "");
  const [message, setMessage] = useState<Message>({ text: "", ok: true });

  const onSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      const { data } = await api.put<User>("/users/me", { fullName });
      saveUser(data);
      setMessage({ text: "Profile updated", ok: true });
    } catch (err) {
      setMessage({ text: getErrorMessage(err, "Update failed"), ok: false });
    }
  };

  return (
    <Card>
      <form onSubmit={onSubmit} className="flex flex-col gap-3.5">
        <h2 className="text-xl font-bold">Edit profile</h2>
        <Label htmlFor="fullName">Full name</Label>
        <TextInput
          id="fullName"
          value={fullName}
          onChange={(e) => setFullName(e.target.value)}
          required
        />
        {message.text &&
          (message.ok ? (
            <SuccessText>{message.text}</SuccessText>
          ) : (
            <ErrorText>{message.text}</ErrorText>
          ))}
        <Button type="submit">Save changes</Button>
        <MutedText>
          <PageLink to="/dashboard">Back to dashboard</PageLink>
        </MutedText>
      </form>
    </Card>
  );
}